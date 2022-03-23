# Content

- This repository includes coding for both Patient and Doctor Android application

- Patient can install demo app via https://install.appcenter.ms/users/dungnguyen27384-gmail.com/apps/ebooking-patient/distribution_groups/beta

- Doctor can install demo app via https://install.appcenter.ms/users/dungnguyen27384-gmail.com/apps/ebooking-doctor/distribution_groups/beta


Please refer to https://github.com/cloud273/mcs-ios for content of application


# Development

1. Git

    + `git clone --recurse-submodules https://cloud273.com:8027/mcs/ios.git`
    + `git pull --recurse-submodules`
    
2. Submodule

    + Add:  `git submodule add https://cloud273.com:8027/library/ios-core.git`
    + Add:  `git submodule add https://cloud273.com:8027/library/ios-localization.git`
    + Add:  `git submodule add https://cloud273.com:8027/library/ios-whiteLabel.git`
    
    +  Remove: 
        - `git submodule deinit <path_to_submodule>`
        - `git rm <path_to_submodule>`
        - `git commit-m "Removed submodule "`
        - `rm -rf .git/modules/<path_to_submodule>`

    + Update
        - `git pull origin HEAD:master`
        - `git push origin HEAD:master`


2. Convert p12 to pem to read information

    + `openssl pkcs12 -in doctor.p12 -out doctor.pem -nodes -clcerts`
    + `openssl pkcs12 -in patient.p12 -out patient.pem -nodes -clcerts`

