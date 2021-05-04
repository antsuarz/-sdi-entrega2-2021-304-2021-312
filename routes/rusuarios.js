module.exports = function(app, swig, gestorBD) {
    app.get("/usuarios", function(req, res) {
        res.send("ver usuarios");
    });

    app.post('/usuario', function(req, res) {
        if(req.body.password != req.body.rePassword){
            res.redirect("/registrarse");
        }
        else {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let usuario = {
                email: req.body.email,
                password: seguro,
                nombre: req.body.nombre,
                apellido: req.body.apellido,
                dinero: 100,
                tipo: "noadmin"
            }
            gestorBD.insertarUsuario(usuario, function (id) {
                if (id == null) {
                    res.redirect("/registrarse?mensaje=Error al registrar el usuario");
                } else {
                    res.redirect("/tienda");
                }
            });
        }
    });


    app.get("/registrarse", function(req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    });


    app.get("/identificarse", function(req, res) {

        swig.renderFile('views/base.html', {usuario: req.session.usuario});
        let respuesta = swig.renderFile('views/bidentificacion.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    });

    app.post("/identificarse", function(req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email : req.body.email,
            password : seguro
        }
        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.redirect("/identificarse" +
                    "?mensaje=Crocolisco"+
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.dinero = usuarios[0].dinero;
                req.session.admin = usuarios[0].tipo;
                if(usuarios[0].tipo == "admin"){
                    res.redirect("/administrador");
                }
                else{
                    res.redirect("/tienda");
                }

            }
        });
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        req.session.dinero = null;
        req.session.admin = null;
        res.redirect("/tienda");
    })


    app.get("/publicaciones", function(req, res) {
        let criterio = { autor : req.session.usuario };
        gestorBD.obtenerOfertas(criterio, function(ofertas) {
            if (ofertas == null) {
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            } else {
                let respuesta = swig.renderFile('views/bpublicaciones.html',
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

    app.get("/administrador", function(req, res) {
        let respuesta = swig.renderFile('views/badministrador.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    });


    app.get("/listaUsuarios", function(req, res) {
        let criterio = {};
        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if (usuarios == null) {
                res.redirect("/tienda" +
                    "?mensaje=Ha ocurrido un error inesperado"+
                    "&tipoMensaje=alert-danger ");
            } else {
                let respuesta = swig.renderFile('views/blistausuarios.html',
                    {
                        usuarios : usuarios,
                        user: req.session.usuario,
                        dinero: req.session.dinero,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

};