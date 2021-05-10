module.exports = function (app, gestorBD) {

    //Funcíon que carga todas las conversaciones de un usuario, cuando este es el comprador
    app.get("/api/conversaciones/comprar", function (req, res) {
        let criterio = {comprador: req.session.usuario};
        gestorBD.obtenerConversacion(criterio, function (conversacionesComprar) {
            if (conversacionesComprar == null) {
                console.log("No ha cargado bien la lista de conversaciones de compra");
            } else {
                res.status(200);
                res.send(JSON.stringify(conversacionesComprar));
            }
        });
    });

    //Funcíon que carga todas las conversaciones de un usuario, cuando este es el vendedor
    app.get("/api/conversaciones/vender", function (req, res) {
        let criterio = {vendedor: req.session.usuario};
        gestorBD.obtenerConversacion(criterio, function (conversacionesVender) {
            if (conversacionesVender == null) {
                console.log("No ha cargado bien la lista de conversaciones de venta");
            } else {
                res.status(200);
                res.send(JSON.stringify(conversacionesVender));
            }
        });
    });


    //Función que elimina conversaciones de la base de datos, y todos los mensajes asociados a ella
    app.delete("/api/conversaciones/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) }
        let criterioMsg = {"conversacion" : gestorBD.mongo.ObjectID(req.params.id) }

        gestorBD.eliminarConversacion(criterio,function(conversaciones){
            if(conversaciones.length == 0)
                console.log(cagaste);
            if ( conversaciones == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(conversaciones) );
            }
        });
        gestorBD.eliminarMensajes(criterioMsg,function(mensajes){
            if ( mensajes == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(mensajes) );
            }
        });



    });



}