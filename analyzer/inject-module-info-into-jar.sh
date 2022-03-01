#!/bin/bash
#
# Yet Another UserAgent Analyzer
# Copyright (C) 2013-2022 Niels Basjes
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an AS IS BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

groupId=$1
artifactId=$2
version=$3

jar -uf "target/${artifactId}-${version}.jar" -C "target/classes" "module-info.class"

STATUS=$?

if [ "${STATUS}" -eq 0 ];
then
  echo "SUCCESS: re-injected the module-info.class"
else
  echo "Something went wrong when trying to inject the module-info.class"
fi
exit "${STATUS}"
