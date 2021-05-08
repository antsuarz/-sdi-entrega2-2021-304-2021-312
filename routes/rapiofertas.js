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

}