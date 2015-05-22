<html>
# ChatRoom
<h1>A miniature Chatting Tool (JAVA Project)</h1>

<h3>Server:</h3>
<p>The server has a main thread and multiple other threads, which is implemented a private class called Channel.
The idea is very straightforward, a main thread, with one and the only serverSocket listing all the connection request. Once there is incoming client, the main thread is gonna start a channel dealing with the actually connection. Meanwhile there are also a broadcast method called whenever a new client is issuing the connection or a client stops working.
</p>

<h3>Client:</h3>
<p>
The client for the most part is a Swing application, whenever a user type in a name and went online, it will receive a online user list(A very annoying bug exsited in this initial commit). Every client has two I would say obvious threads(there are thread running behind the scene), one the the swing, EDT and another is client listening thread. User can talk to as many online users as they want by simply selecting which one or few online user they want to talk with.
</p>
</br>
<p>The bug I mentioned above is the occassional mis-behavior of my listModel, I tried fireInternalAdd method, I even create my customized listModel, none of them worked, Finally realise it's the swing concurrecy problem, will try to fix in second commit</p>


</html>
