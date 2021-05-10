module.exports = function (app, swig, gestorBD) {

    /**
     * Función que te redirige a la ventana de login por defecto
     */
    app.get('/', function (req, res) {
        res.redirect("/identificarse");
    });

    /**
     * Función que inserta un usuario en la base de datos, comprobando antes si ya existe en esta
     */
    app.post('/usuario', function (req, res) {
        if (req.body.password != req.body.rePassword) {
            res.redirect("/registrarse"+
                "?mensaje=La contraseña no se ha repetido correctamente"+
                "&tipoMensaje=alert-danger ");
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
            if (validarCamposRegistro(usuario, req.body.rePassword, req, res)) {

            let criterio = {
                email: req.body.email,
            }
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios == null || usuarios.length != 0) {
                    res.redirect("/registrarse" +
                        "?mensaje=El usuario ya está registrado en la base de datos" +
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
        }
    });

    /**
     * Función encargada de mostrar el formulario de registro
     */
    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin,
        });
        res.send(respuesta);
    });


    /**
     * Función encargada de mostrar el formulario de identificación
     */
    app.get("/identificarse", function (req, res) {

        swig.renderFile('views/base.html', {usuario: req.session.usuario});
        let respuesta = swig.renderFile('views/bidentificacion.html', {
            user: req.session.usuario,
            dinero: req.session.dinero,
            admin: req.session.admin
        });
        res.send(respuesta);
    });

    /**
     * Función encargada de identificar un usuario en la base de datos, para ello comprueba si el usuario está registrado previamente.
     * Dependiendo si el usuario está registrado como administrador, o como usuario normal, muestra la tienda, o la vista de administrador.
     */
    app.post("/identificarse", function (req, res) {

        if(validarCamposIdentificarse(req.body.email, req.body.password, res)) {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let criterio = {
                email: req.body.email,
                password: seguro
            }
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios == null || usuarios.length == 0) {
                    criterioEmail ={
                        email: req.body.email,
                    }
                    gestorBD.obtenerUsuarios(criterioEmail, function (uspass) {
                        if(uspass == null || uspass.length == 0) {
                            res.redirect("/identificarse" +
                                "?mensaje=El usuario no se ha encontrado en la base de datos" +
                                "&tipoMensaje=alert-danger ");
                        }
                        else{
                            res.redirect("/identificarse" +
                                "?mensaje=La contraseña no coincide con la del usuario identificado" +
                                "&tipoMensaje=alert-danger ");
                        }
                    });
                } else {
                    req.session.usuario = usuarios[0].email;
                    req.session.dinero = usuarios[0].dinero;
                    req.session.admin = usuarios[0].tipo;
                    req.session.user = usuarios[0];
                    if (usuarios[0].tipo == "admin") {

                        res.redirect("/administrador");
                    } else {
                        res.redirect("/tienda");
                    }

                }
            });
        }
    });

    /**
     * Función que desconecta un usuario de la aplicación
     * Reinicializa los parametros de sesión.
     */
    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        req.session.dinero = null;
        req.session.admin = null;
        req.session.id = null;
        res.redirect("/identificarse");
    })

    /**
     * Función que obtiene las ofertas que ha publicado el usuario de sesión.
     */
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

    /**
     * Función encargada de mostrar la vista de administrador de la aplicación
     */
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

    /**
     * Función encargada de mostrar la vista de administrador de la aplicación.
     */
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

    /**
     * Función que elimina uno o varios usuarios de la base de datos
     */
    app.post('/listaUsuarios', function (req, res) {
        if(req.body.usuario != null && req.body.usuario.length > 0) {
            if (req.body.usuario[0].length == 1) {
                let criterio = {"_id": gestorBD.mongo.ObjectID(req.body.usuario)};
                eliminarTodoReferenteUsuarios(req.body.usuario,req,res);
                gestorBD.eliminarUsuario(criterio, function (usuarios) {
                    if (usuarios == null) {
                        res.send('Error al eliminar usuarios.');
                    }else{
                        res.redirect('/listaUsuarios');
                    }
                });
            } else {
                for (let i = 0; i < req.body.usuario.length; i++) {
                    let criterio = {"_id": gestorBD.mongo.ObjectID(req.body.usuario[i])};
                    eliminarTodoReferenteUsuarios(req.body.usuario, req, res);
                    gestorBD.eliminarUsuario(criterio, function (usuarios) {
                        if (usuarios == null) {
                            res.send('Error al eliminar usuarios.');
                        }
                    });
                    if (i == req.body.usuario.length - 1) {
                        res.redirect('/listaUsuarios');
                    }

                }
            }
        }
    });

    /**
     * Funcion que se encarga de eliminar cualquier elemento que tenga que ver con un usuario eliminado de la base de datos.
     * Se eliminan sus ofertas publicadas, sus conversaciones y sus mensajes
     * @param usuarios, usuarios que van a ser eliminados
     * @param req
     * @param res
     */
    function eliminarTodoReferenteUsuarios(usuarios, req, res){
        if (usuarios[0].length == 1) {
            eliminarUsuarioBD(usuarios, req, res);
        }
        else{
            for(i = 0; i < usuarios.length; i++){
                eliminarUsuarioBD(usuarios[i], req, res);
            }
        }
    }

    /**
     * Función auxiliar  que elimina un usuario de la base de datos con lo referente a este
     * @param user, usuario a borrar
     * @param req
     * @param res
     */
    function eliminarUsuarioBD(user, req, res){
        let criterio={
            "_id": gestorBD.mongo.ObjectID(user.toString())
        }
        gestorBD.obtenerUsuarios(criterio, function (usuario){
            if(usuario == null || usuario.length == 0){
                res.send('Ha ocurrido un error inesperado');
            }
            else{
                let criterioEliminarOfertas={
                    "autor": usuario[0].email
                }
                let criterioEliminarConversacionesComprar={
                    "comprador": usuario[0].email
                }
                let criterioEliminarConversacionesVender={
                    "vendedor": usuario[0].email
                }
                let criterioEliminarMensajes={
                    "emisor": usuario[0].email
                }
                gestorBD.eliminarOferta(criterioEliminarOfertas, function (ofertas){
                    if(ofertas == null){
                        res.send('Error al borrar ofertas.');
                    }
                })
                gestorBD.eliminarConversacion(criterioEliminarConversacionesComprar, function (converComp){
                    if(converComp == null){
                        res.send('Error al borrar conversaciones de compra.');
                    }
                })
                gestorBD.eliminarConversacion(criterioEliminarConversacionesVender, function (converVen){
                    if(converVen == null){
                        res.send('Error al borrar conversaciones de venta.');
                    }
                })
                gestorBD.eliminarMensajes(criterioEliminarMensajes, function (msg){
                    if(msg == null){
                        res.send('Error al eliminar mensajes');
                    }
                })
            }
        })
    }

    /**
     * Función para validar los campos del formulario de registro de usuarios
     * @param usuario, usuario a validar
     * @param rePassword, contenido del formulario donde se repite la contraseña
     * @param req
     * @param res
     * @returns true si el usuario esta validado correctamente, false si ha fallado algun campo de la validación.
     */
    function validarCamposRegistro(usuario, rePassword, req, res){
        if(usuario.email == ""){
            res.redirect("/registrarse" +
                "?mensaje=El campo email no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.email.length < 10){
            res.redirect("/registrarse" +
                "?mensaje=Email demasiado corto, debe contener más de 10 caracteres" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.nombre == ""){
            res.redirect("/registrarse" +
                "?mensaje=El campo nombre no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.nombre.length < 3){
            res.redirect("/registrarse" +
                "?mensaje=El campo nombre debe contener al menos 3 caracteres" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.apellido == ""){
            res.redirect("/registrarse" +
                "?mensaje=El campo apellido no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.apellido.length < 5){
            res.redirect("/registrarse" +
                "?mensaje=El campo apellido debe contener al menos 5 caracteres" +
                "&tipoMensaje=alert-danger ");
            return false;
        }

        else if(usuario.apellido == ""){
            res.redirect("/registrarse" +
                "?mensaje=El campo apellido no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.password == ""){
            res.redirect("/registrarse" +
                "?mensaje=El campo contraseña no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(usuario.password.length < 8){
            res.redirect("/registrarse" +
                "?mensaje=El campo apellido debe contener al menos 8 caracteres" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(rePassword == ""){
            res.redirect("/registrarse" +
                "?mensaje=Por favor repite tu contraseña" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else
            return true;
    }

    /**
     * Función que valida los campos del formulario de identificación de usuario.
     * @param email, email del usuario
     * @param password, contraseña del usuario
     * @param res
     * @returns true, si los campos son correctos, false, si no lo son
     */
    function validarCamposIdentificarse(email, password, res){
        if(email == ""){
            res.redirect("/identificarse" +
                "?mensaje=El campo email no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else if(password == ""){
            res.redirect("/identificarse" +
                "?mensaje=El campo contraseña no puede estar en blanco" +
                "&tipoMensaje=alert-danger ");
            return false;
        }
        else{
            return true;
        }
    }
};