FROM tomcat:jre8-alpine

####################
ARG APP_VERSION=6.2.1
ENV BAMBOO_HOME /data/bamboo-home
ENV CATALINA_HOME /usr/local/tomcat
ENV GOROOT /usr/lib/go
ENV GOPATH /data/gopath
####################

# Add BAMBOO user
RUN addgroup -g 1000 bamboo
RUN adduser -u 1000 -G bamboo \
      -h /home/bamboo -s /bin/bash \
      -S bamboo

# Install
RUN apk --update add openssl ca-certificates gzip wget git libc-dev go godep && \
	apk add tzdata && \
	cp /usr/share/zoneinfo/America/Chicago /etc/localtime && \
	echo "America/Chicago" > /etc/timezone

#Add ROOT.xml
COPY ROOT.xml $CATALINA_HOME/conf/Catalina/localhost/

#Install BAMBOO
RUN cd $CATALINA_HOME/webapps && rm -rf ROOT \
    && wget -O /tmp "https://maven.atlassian.com/content/groups/public/com/atlassian/bamboo/atlassian-bamboo-web-app/$APP_VERSION/atlassian-bamboo-web-app-$APP_VERSION.war" -O ROOT.war && chmod ug+x $CATALINA_HOME/bin/*.sh
RUN apk del tzdata gzip wget

# Create home directory
RUN mkdir -p -m 0755 $BAMBOO_HOME

#Set Home
RUN cd $CATALINA_HOME/bin/ && echo -n "export CATALINA_OPTS='-Xms512m -Xmx2048m -XX:MaxPermSize=512m -Dbamboo.home=/data/bamboo-home'" >> setenv.sh \
    && . ./setenv.sh && chmod ug+x $CATALINA_HOME/bin/setenv.sh && chown bamboo:bamboo $CATALINA_HOME

#Perms
RUN chown -R bamboo:bamboo $CATALINA_HOME && chown -R bamboo:bamboo $BAMBOO_HOME

#Expose port/agent - so al-agent can connect
EXPOSE 8080 54663

#Set user
USER bamboo

#CMD
CMD $CATALINA_HOME/bin/catalina.sh run