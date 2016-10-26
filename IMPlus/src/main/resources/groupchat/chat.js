/**
 * Created by chengyq on 2016/10/25.
 */
alert("chat.js");

document.writeln("connecting to websocket<br/><br/>");

var socket = new WebSocket('ws://localhost:7778');

socket.onopen = function(event) {

    document.writeln("connected to websocket<br/><br/>");

    
}