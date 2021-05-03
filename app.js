let express = require('express');
let app = express();

let swig = require('swig');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.set('port', 8081);

app.use(express.static('public'));

//Rutas
require("./routes/rusuarios.js")(app, swig);
require("./routes/rofertas.js")(app, swig);


app.listen(app.get('port'), function(){
    console.log('servidor activo');
});