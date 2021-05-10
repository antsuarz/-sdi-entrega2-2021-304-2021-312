module.exports = function(app, gestorBD) {

    //Función que obtiene todas las ofertas de la base de datos
    //Se hace un filtrado para que descarte las pertenecientes a un determinado usuario.
    app.get("/api/oferta", function(req, res) {
        let criterio ={};
        gestorBD.obtenerOfertas( criterio , function(ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                let ofertasFiltradas=[]
                for(i = 0; i < ofertas.length; i++){
                    if(ofertas[i].autor != req.session.usuario)
                        ofertasFiltradas.push(ofertas[i]);
                }
                res.status(200);
                res.send( JSON.stringify(ofertasFiltradas) );
            }
        });
    });

    //Funcion que obtiene ofertas de la base de datos segun un id pasado
    app.get("/api/oferta/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id)}

        gestorBD.obtenerOfertas(criterio,function(ofertas){
            if ( ofertas == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {

                res.status(200);
                res.send(JSON.stringify(ofertas));

            }
        });
    });

    //Función que elimina una oferta de la base de datos
    app.delete("/api/oferta/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id)}

        gestorBD.eliminarOferta(criterio,function(ofertas){
            if ( ofertas == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(ofertas) );
            }
        });
    });

    //Función que agrega mensajes a una conversación, si no existe esta, la crea.
    app.post("/api/mensaje/agregar/:id/:comprador", function (req, res){
        var date = new Date();
        var fechaString = date.getDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();

        let criterioOferta = {
            "_id": gestorBD.mongo.ObjectID(req.params.id),
        };
        //Obtenemos primero la oferta a la que referencia la conversacion, para obtener asi al vendedor,
        // ya que es necesario saber si el usuario de sesion y el autor de la oferta son la misma persona.
        gestorBD.obtenerOfertas(criterioOferta, function (oferta) {
            if(oferta == null){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                let criterio;
                //El problema aqui es obtener el comprador, por lo que se lo pasamos por parámetro
                if(req.session.usuario == oferta[0].autor){
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "vendedor": req.session.usuario,
                        "comprador":req.params.comprador
                    }
                }
                //si resulta que comprador y vendedor son distintos, no necesitamos usar el parámetro
                else {
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "comprador": req.session.usuario,
                        "vendedor": oferta[0].autor
                    };
                }
            gestorBD.obtenerConversacion(criterio, function (lista) {
                if (lista == null) {
                    res.status(500);
                    res.json({
                        error: "No se ha podido generar el listado"
                    })
                } else if (lista.length == 0) {
                    console.log("insertando nueva conversación")
                    insertarConversacionBD(criterio, req, res);
                } else {
                    let mensajeNuevo = {
                        "contenido": req.body.contenido,
                        "fecha": fechaString,
                        "emisor": req.session.usuario,
                        "conversacion": gestorBD.mongo.ObjectID(lista[0]._id),
                        "leido": false
                    }
                    insertarMensajeBD(mensajeNuevo, req, res);
                }
            });
            }
        });
    });

    //Función que nos permite cargar la conversación perteneciente a una oferta en particular, y todos sus mensajes.
    //En el caso de que no haya conversación creada, envia un array vacio.
    app.get("/api/mensaje/:id/:comprador", function (req, res) {
        //Obtenemos primero la oferta a la que referencia la conversacion, para obtener asi al vendedor
        let criterioOferta = {
            "_id": gestorBD.mongo.ObjectID(req.params.id),
        };

        gestorBD.obtenerOfertas(criterioOferta, function (oferta){
            if (oferta == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                let criterio;
                if(req.session.usuario == oferta[0].autor){
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "vendedor": req.session.usuario,
                        "comprador" : req.params.comprador
                    }
                }
                else {
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "comprador": req.session.usuario,
                        "vendedor": oferta[0].autor
                    };
                }

                gestorBD.obtenerConversacion(criterio, function (lista) {
                    if (lista == null ) {
                        res.status(500);
                        res.json({ error: "No se ha podido generar el listado"})
                    }
                    else if(lista.length == 0){

                        console.log("No hay conversación")
                        res.status(200);
                        res.send(JSON.stringify(new Array()));
                    }
                    else {
                        let conversacion = {

                            "conversacion": gestorBD.mongo.ObjectID(lista[0]._id)
                        };
                        gestorBD.obtenerMensajes(conversacion, function (mensajes) {
                            if (mensajes == null) {
                                res.status(500);
                                res.json({
                                    error: "se ha producido un error"
                                })
                            } else {
                                res.status(200);
                                res.send(JSON.stringify(mensajes));
                            }
                        });
                    }
                })
            }
        })
    })

    //Función que crea una nueva conversación y añade, a su vez, el mensaje inicial a esta.
    //En primer lugar se obtiene la oferta a la que pertenecerá la conversación, despues, la creamos
    //Si se crea satisfactoriamente creamos el mensaje iinicial y lo introducimos.
    //por último obtenemos la conversación para que se pueda mostrar en el servidor con el primer mensaje.
    function insertarConversacionBD( criterio, req, res){
        var date = new Date();
        var fechaString = date.getUTCDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let oferta={"_id" : gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(oferta, function (ofertas){
            if ( ofertas == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error obteniendo las ofertas"
                })
            } else {
                let conversacion = {
                    "vendedor": ofertas[0].autor,
                    "comprador": req.session.usuario,
                    "oferta": oferta
                }
                gestorBD.insertarConversacion(conversacion, function (conv) {
                    if ( conv == null ) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error insertando la conversación"
                        })
                    } else {
                        let mensajeNuevo = {
                            "contenido": req.body.contenido,
                            "fecha": fechaString,
                            "emisor": req.session.usuario,
                            "conversacion": conversacion._id,
                            "leido": false
                        }

                        gestorBD.obtenerConversacion(criterio, function(lista){
                            if(lista == null){
                                res.status(500);
                                res.json({
                                    error: "No se ha podido generar el listado"
                                })
                            }
                            else{
                                insertarMensajeBD(mensajeNuevo, req, res);
                            }
                        })
                    }
                })

            }
        })
    }

    //Función que inserta un mensaje nuevo en la base de datos
    function insertarMensajeBD(mensaje,req,res){
        gestorBD.insertarMensaje(mensaje, function (id){
            if (id == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error al insertar el mensaje"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(mensaje));
            }
        });
    }




}