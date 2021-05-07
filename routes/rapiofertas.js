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
                res.status(200);
                res.send( JSON.stringify(ofertas) );
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
                if(ofertas[0].autor != req.body.email) {
                    res.send(JSON.stringify(ofertas[0]));
                }
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

}