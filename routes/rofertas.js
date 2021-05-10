module.exports = function(app, swig, gestorBD) {

    //Funcion que inicializa la ventana tienda
    app.get("/ofertas", function(req, res) {
        let respuesta = swig.renderFile('views/btienda.html', {
            user: req.session.usuario,
            admin: req.session.admin,
            dinero: req.session.dinero,
            ofertas : ofertas
        });
        res.send(respuesta);
    });

    //Funcion para agregar nuevas ofertas,
    //te reenvia a un formulario para introducir los datos
    app.get('/ofertas/agregar', function (req, res) {

        let respuesta = swig.renderFile('views/bagregar.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    })

    //Funcion para mostrar una oferta en concreto segun un id
    app.get('/oferta/:id', function (req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.obtenerOfertas(criterio,function(ofertas){
            if ( ofertas == null ){
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            } else {
                let respuesta = swig.renderFile('views/boferta.html',
                    {
                        oferta : ofertas[0],
                        user: req.session.usuario,
                        dinero: req.session.dinero,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

    //Funcion que inserta una nueva oferta en la base de datos a traves de los datos recogidos de un formulario
    //Se encarga tambien de verificar que los datos son correctos
    app.post("/ofertas", function (req, res) {
    if(req.session.usuario != null || req.session.usuario != undefined ) {
        var date = new Date();
        var fechaString = date.getUTCDay() + "/" + date.getMonth() + "/" + date.getFullYear() + " Hora: " + date.getHours() + ":" + date.getMinutes();
        let oferta = {
            nombre: req.body.nombre,
            detalles: req.body.detalles,
            fecha: fechaString,
            autor: req.session.usuario,
            precio: req.body.precio,
            comprado: false,
            destacada: req.body.destacada
        }
        validarBlancos(oferta,req, res);
        if (oferta.nombre.length < 5) {
            res.redirect("/ofertas/agregar" +
                "?mensaje=El nombre de la oferta es demasiado corto (debe ser superior a 5)." +
                "&tipoMensaje=alert-danger ");
        } else if (oferta.precio <= 0) {
            res.redirect("/ofertas/agregar" +
                "?mensaje=El precio de la oferta debe ser superior a 0." +
                "&tipoMensaje=alert-danger ");

        } else if (oferta.detalles.length < 8) {
            res.redirect("/ofertas/agregar" +
                "?mensaje=Los detalles de la oferta deben contener al menos 8 caracteres" +
                "&tipoMensaje=alert-danger ");
        } else if (oferta.destacada != null) {
            if (req.session.dinero >= 20) {
                let dinero = {"dinero": req.session.dinero - 20};
                req.session.dinero = dinero.dinero;
                let usuarioId = {"_id": gestorBD.mongo.ObjectID(req.session.user._id)};
                gestorBD.modificarDineroUsuario(usuarioId, dinero, function (id) {
                    if (id == null) {
                        res.redirect("/tienda" +
                            "?mensaje=Ha ocurrido un error al modificar el dinero del usuario" +
                            "&tipoMensaje=alert-danger ");
                    } else {
                        insertarOfertaBD(oferta, req, res);
                    }
                });
            } else {
                res.redirect("/ofertas/agregar" +
                    "?mensaje=No puedes comprar esta oferta" +
                    "&tipoMensaje=alert-danger ");
            }
        } else {
            insertarOfertaBD(oferta, req, res);
        }
    }
    else{
        res.redirect("/identificate");
    }
    });


    //Funcion que carga la tienda, gestiona la busqueda y la paginación
    app.get("/tienda", function(req, res) {
        let criterio = {};
        if( req.query.busqueda != null ){
            criterio = { "nombre" :  {$regex : ".*"+req.query.busqueda+".*"}};
        }
        let pg = parseInt(req.query.pg);
        if ( req.query.pg == null){
            pg = 1;
        }
        //TODO cuando hay menos de 3 paginas que salgan solo las paginas que hay
        //TODO arreglar cuando haces busqueda y cambias de pagina siga con la misma busqueda
        //TODO mayusculas y minusc? en busqueda

        gestorBD.obtenerOfertasPg(criterio, pg , function(ofertas, total ) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let ultimaPg = total/4;
                if (total % 4 > 0 ){
                    ultimaPg = ultimaPg+1;
                }
                let paginas = [];
                for(let i = pg-2 ; i <= pg+2 ; i++){
                    if ( i > 0 && i <= ultimaPg){
                        paginas.push(i);
                    }
                }
                let respuesta = swig.renderFile('views/btienda.html',{
                    ofertas : ofertas,
                    paginas : paginas,
                    actual : pg,
                    user: req.session.usuario,
                    dinero: req.session.dinero,
                    admin: req.session.admin,
                    ultimaPg: ultimaPg
                });
                res.send(respuesta);
            }
        });
    });

    //Funcion que elimina una determinada oferta de la base de datos
    app.get('/oferta/eliminar/:id', function (req, res) {
        let criterio = {"_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.eliminarOferta(criterio,function(ofertas){
            if ( ofertas == null ){
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            } else {
                res.redirect("/publicaciones");
            }
        });
    })

    //Función para comprar una determinada oferta
    //Antes de comprar, comprueba la disponibilidad de la oferta, si el comprador no es el autor de esta, y si este tiene suficiente dinero para adquirirla
    //Modifica la oferta, inserta la compra en la base de datos, modifica el valor del dinero del usuario comprador, y actualiza el valor del dinero que se muestra en la interfaz
    app.get('/oferta/comprar/:id', function (req, res) {
        let ofertaId = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(ofertaId, function(oferta){
            if(oferta == null){
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            }
            else{
                if(oferta[0].autor == req.session.usuario){
                    res.redirect("/tienda" +
                        "?mensaje=No puedes comprar una oferta que tu has publicado"+
                        "&tipoMensaje=alert-danger ");
                }
                else if(oferta[0].comprado){
                    res.redirect("/tienda" +
                        "?mensaje=La oferta ya fue vendida"+
                        "&tipoMensaje=alert-danger ");
                }
                else if(oferta[0].precio > req.session.dinero){
                    res.redirect("/tienda" +
                        "?mensaje=No tienes suficiente dinero para comprar esta oferta"+
                        "&tipoMensaje=alert-danger ");
                }
                else{
                    let ofertaModificada = {
                        comprado: true
                    }
                    gestorBD.modificarOferta(ofertaId, ofertaModificada, function (result) {
                        if (result == null) {
                            res.redirect("/tienda" +
                                "?mensaje=Se ha producido un error inesperado al modificar el estado de la oferta"+
                                "&tipoMensaje=alert-danger ");
                        } else {
                            let compra = {
                                usuario : req.session.usuario,
                                ofertaId : ofertaId
                            }
                            gestorBD.insertarCompra(compra ,function(idCompra){
                                if (idCompra == null ){
                                    res.redirect("/tienda" +
                                        "?mensaje=Se ha producido un error al insertar su compra en la base de datos"+
                                        "&tipoMensaje=alert-danger ");
                                } else {
                                    let comprador={"_id": gestorBD.mongo.ObjectID(req.session.user._id)};
                                    let dineroActual = {dinero: req.session.dinero - oferta[0].precio};
                                    gestorBD.modificarDineroUsuario(comprador,dineroActual, function (id){
                                        if (id == null) {
                                            res.redirect("/tienda" +
                                                "?mensaje=Se ha producido un error al modificar el dinero del usuario"+
                                                "&tipoMensaje=alert-danger ");
                                        } else {
                                            let criterio = {
                                                email : req.session.usuario,
                                            }
                                            gestorBD.obtenerUsuarios(criterio, function(usuarios) {
                                                if (usuarios == null || usuarios.length == 0) {
                                                    res.redirect("/identificarse" +
                                                        "?mensaje=No se ha podido identificar al usuario"+
                                                        "&tipoMensaje=alert-danger ");
                                                } else {
                                                    req.session.dinero = usuarios[0].dinero;
                                                    res.redirect("/compras");
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }
                    })

                }
            }
        })
    });

    //Funcion que obtiene las ofertas que un usuario ha comprado, y las muestra.
    app.get("/compras", function(req,res){
        if(req.session.usuario != null) {
            let criterio = {"usuario": req.session.usuario};
            gestorBD.obtenerCompras(criterio, function (compras) {
                if (compras == null) {
                    res.redirect("/tienda" +
                        "?mensaje=Se ha producido un error al obtener un listado con sus compras" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    let ofertasCompradasIds = [];
                    for (i = 0; i < compras.length; i++) {
                        ofertasCompradasIds.push(compras[i].ofertaId._id);
                    }

                    let criterio = {"_id": {$in: ofertasCompradasIds}}
                    gestorBD.obtenerOfertas(criterio, function (ofertas) {
                        let respuesta = swig.renderFile('views/bcompras.html', {
                            ofertas: ofertas,
                            user: req.session.usuario,
                            dinero: req.session.dinero,
                            admin: req.session.admin
                        });
                        res.send(respuesta);
                    });
                }
            });
        }
    })

    //Funcion que obtiene todas las ofertas destacadas del sistema
    app.get("/ofertas/destacadas", function(req,res){
        let criterio = {"destacada" : "on" };
        if( req.query.busqueda != null ){
            criterio = { "nombre" :  {$regex : ".*"+req.query.busqueda+".*"}};
        }

        gestorBD.obtenerOfertas( criterio,function(ofertas) {
            if (ofertas == null) {
                res.redirect("/ofertas/destacadas" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            } else {
                let respuesta = swig.renderFile('views/bdestacadas.html',
                    {
                        ofertas : ofertas,
                        user: req.session.usuario,
                        dinero: req.session.dinero,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    })

    //Funcion que inserta una oferta en la base de datos
    function insertarOfertaBD(oferta, req, res){
            gestorBD.insertarOferta(oferta, function (id) {
                if (id == null) {
                    res.redirect("/tienda" +
                        "?mensaje=Ha ocurrido un error inesperado" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    res.redirect("/publicaciones");

                }
            });
    }

    function validarBlancos(oferta, req, res){
        if(oferta.nombre == ""){
            res.redirect("/ofertas/agregar" +
                "?mensaje=El campo nombre no puede estar vacio" +
                "&tipoMensaje=alert-danger ");
        }
        else if(oferta.detalles == ""){
            res.redirect("/ofertas/agregar" +
                "?mensaje=El campo detalles no puede estar vacio" +
                "&tipoMensaje=alert-danger ");
        }
        else if(oferta.precio == ""){
            res.redirect("/ofertas/agregar" +
                "?mensaje=El campo precio no puede estar vacio" +
                "&tipoMensaje=alert-danger ");
        }
    }
};

