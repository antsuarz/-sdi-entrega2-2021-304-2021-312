let express = require('express');
let app = express();

//Variables
app.set('port', 8081);

//Rutas
require("./routes/rusuarios.js")(app);
require("./routes/rofertas.js")(app);

app.listen(app.get('port'), function(){
    console.log('servidor activo');
});