let express = require('express');
let app = express();

let fs = require('fs');
let https = require('https');

let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

let jwt = require('jsonwebtoken');
app.set('jwt',jwt);
let crypto = require('crypto');
let fileUpload = require('express-fileupload');
app.use(fileUpload());
let mongo = require('mongodb');
let swig = require('swig');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app,mongo);

app.set('port', 8081);
//app.set('db','mongodb://admin:sdi@tiendamusica-shard-00-00.jxgw2.mongodb.net:27017,tiendamusica-shard-00-01.jxgw2.mongodb.net:27017,tiendamusica-shard-00-02.jxgw2.mongodb.net:27017/myWallapop?ssl=true&replicaSet=atlas-od6pn1-shard-0&authSource=admin&retryWrites=true&w=majority');
app.set('db', 'mongodb://admin:sdi@tiendamusica-shard-00-00.any5v.mongodb.net:27017,' +
    'tiendamusica-shard-00-01.any5v.mongodb.net:27017,' +
    'tiendamusica-shard-00-02.any5v.mongodb.net:27017' +
    '/myFirstDatabase?ssl=true' +
    '&replicaSet=atlas-exyqcf-shard-0' +
    '&authSource=admin' +
    '&retryWrites=true' +
    '&w=majority');
app.set('clave','abcdefg');
app.set('crypto',crypto);

//Logger
var log4js = require("log4js");
var logger = log4js.getLogger();
logger.level = "debug";

//Rutas
require("./routes/rusuarios.js")(app, swig, gestorBD, logger);
require("./routes/rofertas.js")(app, swig, gestorBD, logger);
require("./routes/rapiofertas.js")(app, gestorBD, logger);
require("./routes/rapiusuarios.js")(app, gestorBD, logger);
require("./routes/rapiconversaciones.js")(app, gestorBD, logger);


https.createServer({
    key: fs.readFileSync('certificates/alice.key'),
    cert: fs.readFileSync('certificates/alice.crt')
}, app).listen(app.get('port'), function() {
    console.log("Servidor activo");
});

//routerUsuarioAutor
let routerUsuarioAutor = express.Router();
routerUsuarioAutor.use(function(req, res, next) {
    console.log("routerUsuarioAutor");
    let path = require('path');
    let id = path.basename(req.originalUrl);
    gestorBD.obtenerOfertas(
        {_id: mongo.ObjectID(id) }, function (ofertas) {
            console.log(ofertas[0]);
            if(ofertas[0].autor == req.session.usuario ){
                next();
            } else {
                res.redirect("/tienda");
            }
        })
});
//Aplicar routerUsuarioAutor
app.use("/oferta/eliminar",routerUsuarioAutor);

// routerUsuarioToken
let routerUsuarioToken = express.Router();
routerUsuarioToken.use(function(req, res, next) {
    let token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function(err, infoToken) {
            if (err || (Date.now()/1000 - infoToken.tiempo) > 240 ){
                res.status(403); // Forbidden
                res.json({
                    acceso : false,
                    error: 'Token invalido o caducado'
                });
                return;
            } else {
                res.usuario = infoToken.usuario;
                next();
            }
        });
    } else {
        res.status(403); // Forbidden
        res.json({
            acceso : false,
            mensaje: 'No hay Token'
        });
    }
});
app.use('/api/oferta', routerUsuarioToken);


app.use(express.static('public'));
