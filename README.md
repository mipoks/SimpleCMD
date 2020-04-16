# SimpleCMD
This is simple CMD manager.   
The code uses reflection, so <strong>you should not change anything in the written code</strong>.  
There are 2 commands in this project now. 

## Commands
DIR - do the same thing as DIR in CMD.exe. But you can not use additional parameters except the PATH.  
CD - do the same thing as CD in CMD.exe.  But you can not use additional parameters except the PATH.  

### Adding your own command
Create      <strong>CMD/Commands/Thecommand.java</strong>  (just 1 character is capital)
Implement   <strong>CMD/Interfaces/Helpable.java, CMD/Interfaces/Runnable.java</strong>
