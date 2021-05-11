module.exports = function(app, gestorBD, logger) {

    /**
     * Función que obtiene todas las ofertas de la base de datos
     * Se hace un filtrado para que descarte las pertenecientes a un determinado usuario.
     */
    app.get("/api/oferta", function(req, res) {
        let criterio ={};
        gestorBD.obtenerOfertas( criterio , function(ofertas) {
            if (ofertas == null) {
                logger.error("Se ha producido un error obteniendo las ofertas");
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
                logger.info("Ofertas obtenidas con exito");
            }
        });
    });

    /**
     * Funcion que obtiene ofertas de la base de datos según un id
     */
    app.get("/api/oferta/:id", function(req, res) {
        if(autenticado) {
            let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)}

            gestorBD.obtenerOfertas(criterio, function (ofertas) {
                if (ofertas == null) {
                    logger.error("Se ha producido un error obteniendo las ofertas");
                    res.status(500);
                    res.json({
                        error: "se ha producido un error"
                    })
                } else {
                    logger.info("Ofertas obtenidas con exito");
                    res.status(200);
                    res.send(JSON.stringify(ofertas));

                }
            });
        }
    });

    /**
     * Funcion que elimina una oferta de la base de datos
     */
    app.delete("/api/oferta/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id)}

        gestorBD.eliminarOferta(criterio,function(ofertas){
            if ( ofertas == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
                logger.error("Se ha producido un error eliminando la oferta");
            } else {
                res.status(200);
                res.send( JSON.stringify(ofertas) );
                logger.info("Oferta eliminada con exito");
            }
        });
    });

    /**
     * Función que agrega mensajes a una conversación, si no existe esta, la crea.
     *
     */
    app.post("/api/mensaje/agregar/:id/:comprador", function (req, res){
        var date = new Date();
        var fechaString = date.getDate() + "/" +(date.getMonth()+1) + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();

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
                logger.error("Se ha producido un error obteniendo las ofertas");
            } else {
                let criterio;
                //El problema aqui es obtener el comprador, por lo que se lo pasamos por parámetro
                if(req.session.usuario == oferta[0].autor){
                    logger.info("El usuario identificado y el autor de la oferta son el mismo");
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "vendedor": req.session.usuario,
                        "comprador":req.params.comprador
                    }
                }
                //si resulta que comprador y vendedor son distintos, no necesitamos usar el parámetro
                else {
                    logger.info("El usuario identificado y el autor de la oferta son distintos");
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
                    logger.error("No se ha podido generar el listado");
                } else if (lista.length == 0) {
                    logger.info("No hay conversación, hay que insertar una nueva en la base de datos");
                    insertarConversacionBD(criterio, req, res);
                } else {
                    let mensajeNuevo = {
                        "contenido": req.body.contenido,
                        "fecha": fechaString,
                        "emisor": req.session.usuario,
                        "conversacion": gestorBD.mongo.ObjectID(lista[0]._id),
                        "leido": false
                    }
                    logger.info("Insertando mensaje nuevo en la conversación");
                    insertarMensajeBD(mensajeNuevo, req, res);
                }
            });
            }
        });
    });

    /**
     * Función que nos permite cargar la conversación perteneciente a una oferta en particular, y todos sus mensajes.
     * En el caso de que no haya conversación creada, envia un array vacio.
     */
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
                logger.error("Se ha producido un error obteniendo a la oferta");
            } else {
                let criterio;
                if(req.session.usuario == oferta[0].autor){
                    logger.info("El autor y el usuario identificado son el mismo");
                    criterio = {
                        "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
                        "vendedor": req.session.usuario,
                        "comprador" : req.params.comprador
                    }
                }
                else {
                    logger.info("El autor y el usuario identificado son distintos");
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
                        logger.error("No se ha podido generar el listado de conversaciones");
                    }
                    else if(lista.length == 0){
                        res.status(200);
                        res.send(JSON.stringify(new Array()));
                        logger.info("No hay conversacion, mostrando una lista vacia");
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
                                logger.error("Se ha producido un error, obteniendo los mensajes");
                            } else {
                                res.status(200);
                                res.send(JSON.stringify(mensajes));
                                logger.info("Mensajes introducidos con exito");
                            }
                        });
                    }
                })
            }
        })
    })

    /**
     * Función que crea una nueva conversación y añade, a su vez, el mensaje inicial a esta.
     * En primer lugar se obtiene la oferta a la que pertenecerá la conversación, despues, la creamos
     * Si se crea satisfactoriamente creamos el mensaje iinicial y lo introducimos.
     * por último obtenemos la conversación para que se pueda mostrar en el servidor con el primer mensaje.
     *
     * @param criterio, criterio para obtener la conversación una vez insertada
     * @param req
     * @param res
     */
    function insertarConversacionBD(criterio, req, res){
        var date = new Date();
        var fechaString = date.getDate() + "/" +(date.getMonth()+1) + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let oferta={"_id" : gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(oferta, function (ofertas){
            if ( ofertas == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error obteniendo las ofertas"
                })
                logger.error("No se han podido obtener las ofertas");
            } else {
                let conversacion = {
                    "vendedor": ofertas[0].autor,
                    "comprador": req.session.usuario,
                    "oferta": ofertas[0]
                }
                gestorBD.insertarConversacion(conversacion, function (conv) {
                    if ( conv == null ) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error insertando la conversación"
                        })
                        logger.error("Se ha producido un error insertando la conversación");
                    } else {
                        logger.info("Conversación insertada con éxito");
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
                                logger.error("No se ha podido generar el listado de conversaciones");
                            }
                            else{
                                insertarMensajeBD(mensajeNuevo, req, res);
                                logger.info("Mensajes insertados con éxito");
                            }
                        })
                    }
                })

            }
        })
    }

    /**
     * Función que inserta un mensaje nuevo en la base de datos
     * @param mensaje, mensaje a insertar en la base de datos
     * @param req
     * @param res
     */
    function insertarMensajeBD(mensaje,req,res){
        gestorBD.insertarMensaje(mensaje, function (id){
            if (id == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error al insertar el mensaje"
                })
                logger.error("Se ha proucido un error al insertar el mensaje");
            } else {
                res.status(200);
                res.send(JSON.stringify(mensaje));
                logger.info("Mensaje introducido en la base de datos con exito");
            }
        });
    }
}