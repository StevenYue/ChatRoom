<html>
<h1>Chat Room</h1>
<p>This is a very light version chatting tool, programmed in JAVA. However it supports a lot of important and useful
features, like, constantly update online user list, and broadcast message to multiple users.
</p>

<div>
<p><font color="red">1. new user came online</p>
<img src="screenshots\onlineUserBroadCasting.png"  width="50%" height="50%">
<p><font color="red">2.online user disconnected</p>
<img src="screenshots\onlineUserBroadCasting2.png"  width="50%" height="50%">
</div>

<div>
<p><font color="red">3.Single target communication</p>
<img src="screenshots\singleTargetUser.png"  width="50%" height="50%">
</br>
<p><font color="red">4.Multiple targets communication</p>
<img src="screenshots\MultiplyTargerUser.png"  width="50%" height="50%">
</div>

<h3>Update of this commit</h3>
<p>finally fix the bug  of Jlist rendering, the solution is using swingutility.invokelater method to undate UI whenever
an update is ready and required, this will guarantee all UI rendering is in Event Dispatch thread. other than this fix,
the rest  are just fine.</p>

</html>
