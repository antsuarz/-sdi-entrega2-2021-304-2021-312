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
                res.send("Error al recuperar la oferta.");
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
        var fechaString = date.getDay() + "/" +date.getMonth() + "/"+ date.getFullYear()+" Hora: " + date.getHours()+":"+date.getMinutes();
        let oferta = {
            nombre: req.body.nombre,
            detalles: req.body.detalles,
            fecha: fechaString,
            autor: req.session.usuario,
            precio:req.body.precio
        }
        // Conectarse
        gestorBD.insertarOferta(oferta, function(id){
            if (id == null) {
                res.send("Error al insertar la oferta");
            } else {
                if (req.files.foto != null) {
                    var imagen = req.files.foto;
                    imagen.mv('public/fotos/' + id + '.png', function(err) {
                        if (err) {
                            res.send("Error al subir la foto");
                        } else {
                            res.send("Agregada id: " + id);
                        }
                    });
                }

            }
        });
    });

    app.get("/tienda", function(req, res) {
        let criterio = {};
        if( req.query.busqueda != null ){
            criterio = { "nombre" :  {$regex : ".*"+req.query.busqueda+".*"} };
        }

        gestorBD.obtenerOfertas( criterio,function(ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let respuesta = swig.renderFile('views/btienda.html',
                    {
                        ofertas : ofertas,
                        user: req.session.usuario,
                        dinero: req.session.dinero,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

};

