
# Déploiement de l'application esupstage

## Type d'application

Application Spring-boot java

## Prérequis

* Tomcat 9
* Java 11
* Maven 3

## Dépendances

* Module utils-common

## Configuration

### Fichiers de configuration

* Fichier de configuration de l'application : application-(test|prod).properties : 



Ce fichier de configuration doit être déposé dans le répertoire /src/main/resources de l'application.

### Cache

Les fichiers  de cache seront créés dans  le répertoire java.io.tmpdir/esupstage


## Déploiement de l'application

### Construction
    mvn clean package /* génération du WAR target/esupstage.war */


### Dépendances 

#### Module utils-common

Ce module est utilisé par l'ensemble des applications Spring-boot.

##### Sources 
https://gitlab-dsi.univ-lille.fr/communication-web/springboot-utils [branche:master]

##### Construction
Répertoire racine du projet :

    mvn clean install

#### Sources de l'application

https://gitlab-dsi.univ-lille.fr/dsn/esupstage [branche:master]

##### Construction
Répertoire racine du projet :

    mvn clean install