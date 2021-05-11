module.exports = function (app, gestorBD, logger) {

    /**
     * Funcíon que carga todas las conversaciones de un usuario, cuando este es el comprador
     */
    app.get("/api/conversaciones/comprar", function (req, res) {
        let criterio = {comprador: req.session.usuario};
        gestorBD.obtenerConversacion(criterio, function (conversacionesComprar) {
            if (conversacionesComprar == null) {
                logger.error("No ha cargado bien la lista de conversaciones de compra");
            } else {
                res.status(200);
                res.send(JSON.stringify(conversacionesComprar));
                logger.info("Ha cargado bien la lista de conversaciones de compra");
            }
        });
    });
    /**
     * Funcíon que carga todas las conversaciones de un usuario, cuando este es el vendedor
     *
     */
    app.get("/api/conversaciones/vender", function (req, res) {
        let criterio = {vendedor: req.session.usuario};
        gestorBD.obtenerConversacion(criterio, function (conversacionesVender) {
            if (conversacionesVender == null) {
                logger.error("No ha cargado bien la lista de conversaciones de venta");
            } else {
                res.status(200);
                res.send(JSON.stringify(conversacionesVender));
                logger.info("Conversciones obtenidas con exito");
            }
        });
    });


    /**
     * Función que elimina conversaciones de la base de datos, y todos los mensajes asociados a ella
     */
    app.delete("/api/conversaciones/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) }
        let criterioMsg = {"conversacion" : gestorBD.mongo.ObjectID(req.params.id) }

        gestorBD.eliminarMensajes(criterioMsg,function(mensajes){
            if ( mensajes == null ){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
                logger.error("Se ha producido un error eliminando los mensajes");
            } else {
                logger.info("Mensajes eliminados con éxito");
                gestorBD.eliminarConversacion(criterio,function(conversaciones){
                    if(conversaciones.length == 0)
                        console.log(cagaste);
                    if ( conversaciones == null ){
                        res.status(500);
                        res.json({
                            error : "se ha producido un error"
                        })
                        logger.error("Se ha producido un error al eliminar la conversación");
                    } else {
                        res.status(200);
                        res.send( JSON.stringify(conversaciones) );
                        logger.info("Conversacion eliminada con éxito");
                    }
                });
            }
        });
    });
}