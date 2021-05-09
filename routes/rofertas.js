module.exports = function(app, swig, gestorBD) {
    app.get("/ofertas", function(req, res) {


        let respuesta = swig.renderFile('views/btienda.html', {
            user: req.session.usuario,
            admin: req.session.admin,
            dinero: req.session.dinero,
            ofertas : ofertas
        });
        res.send(respuesta);
    });
    app.get('/ofertas/agregar', function (req, res) {

        let respuesta = swig.renderFile('views/bagregar.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    })


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

    app.post("/ofertas", function (req, res) {

        var date = new Date();
        var fechaString = date.getUTCDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let oferta = {
            nombre: req.body.nombre,
            detalles: req.body.detalles,
            fecha: fechaString,
            autor: req.session.usuario,
            precio:req.body.precio,
            comprado: false,
            destacada: req.body.destacada
        }
        if (oferta.precio <= 0) {
            res.redirect("/oferta/agregar" +
                "?mensaje=El precio de la oferta debe ser superior a 0."+
                "&tipoMensaje=alert-danger ");
        }
        if(oferta.destacada != null){
            if (req.session.dinero >= 20) {
                let dinero = {"dinero": req.session.dinero - 20};
                req.session.dinero = dinero.dinero;
                let usuarioId = {"_id": gestorBD.mongo.ObjectID(req.session.user._id)};
                gestorBD.modificarDineroUsuario(usuarioId, dinero,function (id) {
                    if (id == null) {
                        res.redirect("/tienda" +
                            "?mensaje=Ha ocurrido un error al modificar el dinero del usuario"+
                            "&tipoMensaje=alert-danger ");
                    } else {
                        insertarUsuarioBD(oferta, req ,res);
                    }
                });
            } else {
                res.redirect("/ofertas/agregar" +
                    "?mensaje=No puedes comprar esta oferta"+
                    "&tipoMensaje=alert-danger ");
            }
        }
        else {
            insertarUsuarioBD(oferta, req, res);
        }
    });




    app.get("/tienda", function(req, res) {
        let criterio = {};
        if( req.query.busqueda != null ){
            criterio = { "nombre" :  {$regex : ".*"+req.query.busqueda+".*"}};
        }
        let pg = parseInt(req.query.pg);
        if ( req.query.pg == null){
            pg = 1;
        }

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

    app.get("/compras", function(req,res){
        let criterio = {"usuario" : req.session.usuario};
        gestorBD.obtenerCompras(criterio, function (compras){
            if(compras == null){
                res.redirect("/tienda" +
                    "?mensaje=Se ha producido un error al obtener un listado con sus compras"+
                    "&tipoMensaje=alert-danger ");
            }
            else {
                let ofertasCompradasIds = [];
                for(i=0; i < compras.length; i++){
                    ofertasCompradasIds.push(compras[i].ofertaId._id);
                }

                let criterio = {"_id" : {$in: ofertasCompradasIds}}
                gestorBD.obtenerOfertas(criterio, function (ofertas){
                    let respuesta = swig.renderFile('views/bcompras.html', {
                        ofertas : ofertas,
                        user: req.session.usuario,
                        dinero: req.session.dinero,
                        admin: req.session.admin
                    });
                    res.send(respuesta);
                });
            }
        });
    })

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

    function insertarUsuarioBD(oferta, req, res){
        gestorBD.insertarOferta(oferta, function (id) {
            if (id == null) {
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado" +
                    "&tipoMensaje=alert-danger ");
            } else {
                if (req.files.foto != null) {
                    var imagen = req.files.foto;
                    imagen.mv('public/fotos/' + id + '.png', function (err) {
                        if (err) {
                            res.redirect("/oferta/agregar" +
                                "?mensaje=Se ha producido un error al cargar la imagen"+
                                "&tipoMensaje=alert-danger ");
                        } else {
                            res.redirect("/publicaciones");
                        }
                    });
                }

            }
        });
    }

};

