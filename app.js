let express = require('express');
let app = express();

let mongo = require('mongodb');
let swig = require('swig');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.set('port', 8081);
app.set('db','mongodb://admin:sdi@tiendamusica-shard-00-00.jxgw2.mongodb.net:27017,tiendamusica-shard-00-01.jxgw2.mongodb.net:27017,tiendamusica-shard-00-02.jxgw2.mongodb.net:27017/myWallapop?ssl=true&replicaSet=atlas-od6pn1-shard-0&authSource=admin&retryWrites=true&w=majority');

app.use(express.static('public'));

//Rutas
require("./routes/rusuarios.js")(app, swig);
require("./routes/rofertas.js")(app, swig, mongo);


app.listen(app.get('port'), function(){
    console.log('servidor activo');
});