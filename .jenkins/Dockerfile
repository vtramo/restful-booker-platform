FROM maven:3.9.5-eclipse-temurin-21

# Install Docker dependencies
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common \
    wget

# Add Docker GPG key and repository
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Install Docker (latest version)
RUN apt-get update && \
    apt-get install -y docker-ce docker-ce-cli containerd.io

# Install python
RUN apt-get install -y python3 pip virtualenv

# Install bzt for Performance Testing
RUN pip install --upgrade wheel setuptools Cython bzt

# Install JMeter
ARG JMETER_VERSION="5.6.2"
ARG JMETER_DIR="apache-jmeter-${JMETER_VERSION}"
ARG JMETER_TGZ="${JMETER_DIR}.tgz"
ARG JMETER_URL="https://dlcdn.apache.org//jmeter/binaries/${JMETER_TGZ}"
RUN curl -LO "${JMETER_URL}" && tar -xzf "$(basename "${JMETER_URL}")" && rm -rf "${JMETER_TGZ}"
ENV JMETER_HOME="/${JMETER_DIR}/bin"

# Install Chrome (Selenium)
RUN apt-get install -y gnupg
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
RUN apt-get update && apt-get -y install google-chrome-stable

CMD ["/bin/bash"]
