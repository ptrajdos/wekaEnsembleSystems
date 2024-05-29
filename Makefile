ROOTDIR=$(realpath $(dir $(firstword $(MAKEFILE_LIST))))
LOCAL_MVN_REPO=${ROOTDIR}/tmp_m2
MVN=mvn
JAVA=java

WEKA_JAR_PATH_INT=${WEKA_JAR_PATH}#WEKA_JAR_PATH should be set as environment variable

DIST_PATH=${ROOTDIR}/dist
PACKAGE_VERSION=$(shell ${MVN} help:evaluate -Dexpression=project.version -q -DforceStdout)
PACKAGE_NAME=$(shell ${MVN} help:evaluate -Dexpression=project.artifactId -q -DforceStdout)

PACKAGE_PATH=${DIST_PATH}/${PACKAGE_NAME}-${PACKAGE_VERSION}-package.zip



.PHONY: install clean

install_package:
	${MVN} -T 1C clean package

clean:
	rm -rf ${LOCAL_MVN_REPO}
	${MVN} clean

install_package_fresh: clean
	mkdir -p ${LOCAL_MVN_REPO}
	${MVN} -T 1C package -Dmaven.repo.local=${LOCAL_MVN_REPO}


install: install_package
	${JAVA} -cp ${WEKA_JAR_PATH_INT} weka.core.WekaPackageManager -install-package ${PACKAGE_PATH}

install_built:
	${JAVA} -cp ${WEKA_JAR_PATH_INT} weka.core.WekaPackageManager -install-package ${PACKAGE_PATH}

maven_purge:
	${MVN} org.apache.maven.plugins:maven-dependency-plugin:2.1:purge-local-repository  -DreResolve=true -DactTransitively=true -Dverbose=true
