Creer une application basée sur les services métiers:
  - Service de gestions des clients et fournisseurs
  - Service de gestion des inventaires
  - Service de gestion des commandes
  - Service de gestion des receptions d'article/Service
  - Service de gestion des factures
  - Service de gestion des demande d'achat
  - Service de gestion des factures
  - Service de notification (utilisation du broker kafka)
  - Service d'authentification
L'orchestration des services se fait via les services techniques de Spring Cloud :
- Spring Cloud Gateway comme service proxy
- Consul Registry Sericeç=
- Consult Config, Spring Cloud Config
- vault Config (Partage des secrets)
- Resilience4j : CircuitBreacker et Retry
Le principe SOLID nous aident à écrire du code propre qui améliore la testabilité du code. En effet, le code est modulaire et faiblement couplé.
Pour ce qui est de la sécurité, nous allons utiliser Spring Sécurity et JWT.
La seconde phase consistera à mettre en place l'integration Continue et la livraison continue en utilisant les outils telques :
- Docker pour la contenerisation de nos differents services
- Jenkins : Pour l'automatisation
- SonarQube : pour la evaluer la qualité de notre code source
- Kubernates
