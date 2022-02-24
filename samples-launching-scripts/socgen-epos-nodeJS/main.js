var http = require("http");
var EposInterface = require('./EposInterface');
const application = require('./application.json')

http.createServer(function (request, response) {
   // Send the HTTP header 
   // HTTP Status: 200 : OK
   // Content Type: text/plain
   response.writeHead(200, {'Content-Type': 'text/plain'});
   console.log("start");
   
  EposInterface.sendLoanApplication(application);

   response.end(  'eposResonse');
}).listen(8081);

console.log('Server running at http://127.0.0.1:8081/');