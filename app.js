let express = require('express');
let app = express();

app.set('port', 8081);

app.get('/usuarios', function(req, res){
    res.send('ver usuarios');
});

app.get('/ofertas', function(req, res){
    res.send('ver ofertas');
});

app.listen(app.get('port'), function(){
    console.log('servidor activo');
});