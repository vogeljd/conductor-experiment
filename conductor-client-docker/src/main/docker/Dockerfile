FROM registry.imanage.com/m-java8
MAINTAINER Jim Vogel

RUN mkdir /usr/local/share/workflow

RUN apt-get update && apt-get install -y jq curl;

COPY workflow-client.jar /usr/local/share/workflow/workflow-client.jar
COPY start-client.sh /usr/local/share/workflow/start-client.sh

# Add the Vault secure initialization scripts to the docker image. This securely transfers a Vault
# token to this docker image at runtime and enables our cdoe to read vault secrets via the
# Java SecretReader interface
ADD vault-scripts.tar.gz /vault-scripts

RUN chmod a+x /usr/local/share/workflow/start-client.sh

ENTRYPOINT ["/usr/local/share/workflow/start-client.sh"]