buildName=$(echo $1 | sed -e 's/\//_/g')
curl --header "Content-Type: application/json" \
  --header "Authorization: Bearer $CCV2_API_TOKEN" \
  --request POST \
  --data '{"applicationCode": "commerce-cloud","branch": "'$1'","name": "'$buildName'-'$2'"}' \
  https://portalrotapi.hana.ondemand.com/v2/subscriptions/$CCV2_SUBSCRIPTION_KEY/builds
