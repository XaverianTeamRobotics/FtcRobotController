So OpModes are classes, correct?
Instead, OpModes are based off of Jlooping, because I said so.

You get access to the normal ScriptParameters that jlooping offers by accessing the **environment**
variable in the OpMode.

The **construct** method is run before the OpMode is started, and the **run** method is run continously
while the OpMode is running. THIS SHOULD NOT HAVE A LOOP INSIDE OF IT!!!!!!!!