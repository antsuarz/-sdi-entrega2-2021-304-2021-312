module.exports = function(app, swig, mongo) {
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

        let respuesta = swig.renderFile('views/btienda.html', {
            vendedor : 'Tienda',
            ofertas : ofertas
        });
        res.send(respuesta);
    });
    app.get('/ofertas/agregar', function (req, res) {
        let respuesta = swig.renderFile('views/bagregar.html', {

        });
        res.send(respuesta);
    })

    app.get('/ofertas/:id', function(req, res) {
        let respuesta = 'id: ' + req.params.id;
        res.send(respuesta);
    });

    app.post("/ofertas", function (req, res) {
        var date = new Date();
        var fechaString = date.getDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let oferta = {
            nombre: req.body.nombre,
            detalles: req.body.detalles,
            fecha: fechaString,
            precio:req.body.precio
        }
        // Conectarse
        mongo.MongoClient.connect(app.get('db'), function(err, db) {
            if (err) {
                res.send("Error de conexión: " + err);
            } else {
                let collection = db.collection('ofertas');
                collection.insertOne(oferta, function(err, result) {
                    if (err) {
                        res.send("Error al insertar la oferta " + err);
                    } else {
                        res.send("oferta agregada con id: "+ result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    });
};
