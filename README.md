# univ-lr_PROJET_Debathon
A student project for the association CDIJ to debate

You can find two modules (originally 3, but the last one with the commons part has been merged with the two others).

- Debathon_application
> main : <br>
>server/src/main/java/fr/univlr/debathon/application/Main.java

- Debathon_server
> main : <br>
>server/src/main/java/fr/univlr/debathon/server/Main.java

Be careful if you execute with a IDE to select the right module.

It's possible with this version to compile each module into a executable .jar file with maven.
It will create a uber-jar, with all the dependencies.
This is useful to be cross-platform and will encapsulate the server for the module <Debathon_server>.

A user's manual is available at the root of this project.