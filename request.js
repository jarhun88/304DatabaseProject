const axios = require('axios')

module.exports {
    function request() {
        axios.post().then((response) => {
            console.log(response)
        })
    }
}