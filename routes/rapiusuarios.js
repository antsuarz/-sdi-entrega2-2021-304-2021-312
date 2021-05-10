module.exports = function(app, gestorBD) {

    //Función que autentica a un usuario en la aplicacion, comprueba si esta en la base de datos, si es asi, marca al usuario como autenticado.
    app.post("/api/autenticar", function(req,res){
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave')).update(req.body.password).digest('hex');
        let criterio= {
            email : req.body.email,
            password:seguro
        }
        let usuarioIdentificar= {
            email : req.body.email,
            password:req.body.password
        }
        validaDatosUsuario(usuarioIdentificar, function(errors){
            if (errors !== null && errors.length > 0) {
                res.status(403);
                res.json({
                    errores: errors
                })
            } else {
                gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                    if (usuarios == null || usuarios.length == 0) {
                        let elProblemaEsElPassword={
                            email : req.body.email
                        }
                        gestorBD.obtenerUsuarios(elProblemaEsElPassword, function (list){
                            if(list.length != 0){
                                let errors = new Array();
                                errors.push("La contraseña introducida es incorrecta")
                                res.status(403);
                                res.json({
                                    errores: errors
                                })
                            }
                            else{
                                res.status(401);
                                res.json({
                                    autenticado: false
                                })
                            }
                        });

                    } else {
                        let token = app.get('jwt').sign(
                            {usuario: criterio.email, tiempo: Date.now() / 1000},
                            "secreto");
                        req.session.usuario = criterio.email;
                        res.status(200);
                        res.json({
                            autenticado: true,
                            token: token
                        })
                    }
                });
            }
        })
    });


    function validaDatosUsuario(usuario, funcionCallback) {
        let errors = new Array();
        if (usuario.email === null || typeof usuario.email === 'undefined' ||
            usuario.email === "")
            errors.push("El campo email no puede estar vacio")
        if (usuario.password === null || typeof usuario.password === 'undefined' ||
            usuario.password === "")
            errors.push("El campo contraseña no puede estar vacio")
        if (errors.length <= 0)
            funcionCallback(null)
        else
            funcionCallback(errors)
    }
}