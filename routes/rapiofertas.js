module.exports = function(app, gestorBD) {

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

    app.post("/api/mensaje/agregar/:id", function (req, res){
        var date = new Date();
        var fechaString = date.getUTCDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let criterio = {
            "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
            "comprador": req.session.usuario
        };
        gestorBD.obtenerConversacion(criterio, function(lista){
           if(lista == null){
               res.status(500);
               res.json({
                   error: "No se ha podido generar el listado"
               })
           }
           else if(lista.length == 0){
               console.log("xd")
               insertarConversacionBD(req, res);
           }
           else{
               let mensajeNuevo = {
                   "contenido": req.body.contenido,
                   "fecha": fechaString,
                   "emisor": req.session.usuario,
                   "conversacion": gestorBD.mongo.ObjectID(lista[0]._id),
                   "leido": false
               }
               insertarMensajeBD(mensajeNuevo, req ,res);
           }
        });
    });

    app.get("/api/mensaje/:id", function (req, res) {
        let criterio = {
            "oferta._id": gestorBD.mongo.ObjectID(req.params.id),
            "comprador": req.session.usuario
        };

        gestorBD.obtenerConversacion(criterio, function (lista) {
            if (lista == null ) {
                res.status(500);
                res.json({
                    error: "No se ha podido generar el listado"
                })
            }
            else if(lista.length == 0){
                console.log("xd")
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
    })

    function insertarConversacionBD( req, res){
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
                gestorBD.insertarConversacion(conversacion, function (result) {
                    if ( conversacion == null ) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error insertando la conversación"
                        })
                    } else {
                        let mensajeNuevo = {
                            "contenido": req.body.contenido,
                            "fecha": fechaString,
                            "emisor": req.session.usuario,
                            "conversacion": conversacion,
                            "leido": false
                        }
                        insertarMensajeBD(mensajeNuevo, req, res);
                    }
                })

            }
        })
    }
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