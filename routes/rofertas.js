module.exports = function(app, swig) {
    app.get("/ofertas", function(req, res) {
        let ofertas= [{
            "nombre": "cosa",
            "descripcion": "una cosa",
            "precio": "2"
        }, {
            "nombre": "cosa2",
            "descripcion": "otra cosa",
            "precio": "2"
        }];

        let respuesta = swig.renderFile('views/tienda.html', {
            vendedor : 'Tienda',
            ofertas : ofertas
        });
        res.send(respuesta);
    });

    app.post("/ofertas", function (req, res) {
        var date = new Date();
        var fechaString = date.getDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        res.send("Oferta agregada: "+req.body.nombre + "<br>" + " Detalles: "+ req.body.detalles + "<br>" + "Fecha: "+ fechaString + "<br>" + " Precio: "+ req.body.precio + "<br>");
    });
};
