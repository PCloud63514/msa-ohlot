#! /bin/bash
MSG_1="서비스 실행"
MSG_2="실행 서비스 목록 조회"
MSG_3="실행 서비스 종료"
MSG_4="서비스 파일 build"
MSG_5="Docker Image 배포"
MSG_6="build & Docker Image 배포"

function applications_show() {
  echo "==== 서비스 목록 ===="
  SERVICES=($(ls -d applications/* | cut -f 2 -d '/' | grep -e 'app-*' -e 'web-*'))
  # shellcheck disable=SC2068
  for service in ${SERVICES[@]}; do
    echo ${service}
  done
  echo "====================="
}

function read_applications() {
  echo "실행하고자 하는 서비스를 입력해주세요. (배열입력 또는 입력하지 않을 경우 전체 서비스 실행입니다.)"
  read -a READ_SERVICES -p "입력(ENTER) > "

  if [ -z "$READ_SERVICES" ]; then
    READ_SERVICES+=(${SERVICES[@]})
  fi
  clear
  echo "==== 대상 서비스 확인 ===="

  for service in ${READ_SERVICES[@]} ; do
    echo ${service}
  done
  echo "=========================="
}

function build_run() {
  clear
  echo "[${MSG_1}]"
  applications_show
  read_applications

  for appName in ${READ_SERVICES[@]}; do
    echo ${appName} "build start"
    echo `nohup ./gradlew applications:${appName}:bootRun &`
  done
    echo "good!!!"

}

function build_applications() {
  echo "[${MSG_4}]"
  applications_show
  read_applications

  for service in ${READ_SERVICES[@]}; do
    if [[ ${service} =~ app- ]]; then
      echo "${service} build"
      bash ./gradlew applications:${service}:clean
      bash ./gradlew applications:${service}:bootJar
    elif [[ ${service} =~ web- ]]; then
      echo  "${service} npm build"
      cd applications/${service}
      echo `pwd`
      npm install
      npm run build
      cd ../..
    fi
  done
  echo "[${MSG_4} 완료.]"

}

function docker_image_deployment() {
  echo "[${MSG_5}]"
}

function build_and_docker_image_deployment() {
  echo "[${MSG_6}]"
}

echo "[1]. ${MSG_1}"
echo "[2]. ${MSG_2}"
echo "[3]. ${MSG_3}"
echo "[4]. ${MSG_4}"
echo "[5]. ${MSG_5}"
echo "[6]. ${MSG_6}"

# shellcheck disable=SC2162
read CHOICE
clear

case ${CHOICE} in
1)
  build_run
  ;;
4)
  build_applications
  ;;
5)
  docker_image_deployment
  ;;
6)
  build_and_docker_image_deployment
  ;;
*)
  echo "Error"
  ;;
esac

echo "스크립트 종료."