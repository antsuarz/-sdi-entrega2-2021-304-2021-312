module.exports = function (app, swig, gestorBD) {

    //Función que te redirige a la ventana de login por defecto
    app.get('/', function (req, res) {
        res.redirect("/identificarse");
    });

    //Función que inserta un usuario en la base de datos, comprobando antes si ya existe en esta
    app.post('/usuario', function (req, res) {
        if (req.body.password != req.body.rePassword) {
            res.redirect("/registrarse");
        } else {
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
            let criterio = {
                email : req.body.email,
            }
            gestorBD.obtenerUsuarios(criterio, function(usuarios) {
                if (usuarios == null || usuarios.length != 0) {
                    res.redirect("/registrarse" +
                        "?mensaje=El usuario ya está registrado en la base de datos"+
                        "&tipoMensaje=alert-danger ");
                } else {
                    gestorBD.insertarUsuario(usuario, function (id) {
                        if (id == null) {
                            res.redirect("/registrarse?mensaje=Error al registrar el usuario");
                        } else {
                            req.session.usuario = usuario.email;
                            req.session.dinero = usuario.dinero;
                            req.session.admin = usuario.tipo;
                            req.session.user = usuario;
                            res.redirect("/tienda");
                        }
                    });
                }
            });
        }
    });

    //Función encargada de mostrar el formulario de registro
    app.get("/registrarse", function (req, res) {
        //TODO sacar error por pantalla al intentar registrarse
        let respuesta = swig.renderFile('views/bregistro.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin,
        });
        res.send(respuesta);
    });


    //Función encargada de mostrar el formulario de identificación
    app.get("/identificarse", function (req, res) {

        swig.renderFile('views/base.html', {usuario: req.session.usuario});
        let respuesta = swig.renderFile('views/bidentificacion.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    });

    //Función encargada de identificar un usuario en la base de datos, para ello comprueba si el usuario está registrado previamente
    //Dependiendo si el usuario está registrado como administrador, o como usuario normal, muestra la tienda, o la vista de administrador
    app.post("/identificarse", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email: req.body.email,
            password: seguro
        }
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.redirect("/identificarse" +
                    "?mensaje=El usuario no se ha encontrado en la base de datos"+
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.dinero = usuarios[0].dinero;
                req.session.admin = usuarios[0].tipo;
                req.session.user = usuarios[0];
                if(usuarios[0].tipo == "admin"){

                    res.redirect("/administrador");
                } else {
                    res.redirect("/tienda");
                }

            }
        });
    });

    //Función que desconecta un usuario de la aplicación
    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        req.session.dinero = null;
        req.session.admin = null;
        req.session.id = null;
        res.redirect("/identificarse");
    })

    //Función que obtiene las ofertas que ha publicado el usuario de sesión
    app.get("/publicaciones", function (req, res) {
        if(req.session.usuario != null) {
            let criterio = {autor: req.session.usuario};
            gestorBD.obtenerOfertas(criterio, function (ofertas) {
                if (ofertas == null) {
                    res.redirect("/tienda" +
                        "?mensaje=Ha ocurrido un error inesperado" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    let respuesta = swig.renderFile('views/bpublicaciones.html',
                        {
                            ofertas: ofertas,
                            user: req.session.usuario,
                            dinero: req.session.dinero,
                            admin: req.session.admin
                        });
                    res.send(respuesta);
                }
            });
        }
    });

    //Función encargada de mostrar la vista de administrador de la aplicación
    app.get("/administrador", function (req, res) {
        if(req.session.admin == "admin") {
            let respuesta = swig.renderFile('views/badministrador.html', {
                user: req.session.usuario,
                dinero: req.session.dinero,
                admin: req.session.admin
            });
            res.send(respuesta);
        }
    });

    //Función que muestra la lista de usuarios registrados en la base de datos
    app.get("/listaUsuarios", function (req, res) {
        if(req.session.admin == "admin") {
            let criterio = {};
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios == null) {
                    res.redirect("/tienda" +
                        "?mensaje=Ha ocurrido un error inesperado" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    let respuesta = swig.renderFile('views/blistausuarios.html',
                        {
                            usuarios: usuarios,
                            user: req.session.usuario,
                            dinero: req.session.dinero,
                            admin: req.session.admin
                        });
                    res.send(respuesta);
                }
            });
        }
    });

    //Función que elimina uno o varios usuarios de la base de datos
    app.post('/listaUsuarios', function (req, res) {
        console.log(req.body.usuario);
        if(req.body.usuario != null && req.body.usuario.length > 0) {
            if (req.body.usuario[0].length == 1) {
                let criterio = {"_id": gestorBD.mongo.ObjectID(req.body.usuario)};
                gestorBD.eliminarUsuario(criterio, function (usuarios) {
                    if (usuarios == null) {
                        res.send('Error al eliminar usuarios.');
                    }else{
                        res.redirect('/listaUsuarios');
                    }
                });
            } else {
                for (let i = 0; i < req.body.usuario.length; i++) {
                    console.log(req.body.usuario[i]);

                    let criterio = {"_id": gestorBD.mongo.ObjectID(req.body.usuario[i])};
                    gestorBD.eliminarUsuario(criterio, function (usuarios) {
                        if (usuarios == null) {
                            res.send('Error al eliminar usuarios.');
                        }
                    });
                    if (i == req.body.usuario.length - 1)
                        res.redirect('/listaUsuarios');
                }
            }
        }
    });
};