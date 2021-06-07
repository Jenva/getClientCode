
var exec = require('cordova/exec');
var channel = require('cordova/channel');
var config = ''

    channel.onCordovaReady.subscribe(function () {
        exec((res) => {
            config = res
        },  function (msg) {
            console.error('BuildInfo init fail');
            console.error(msg);
        }, 'getClientCode', 'getBuildConfig', ['CLIENT_CODE']);
    })



function getConfig () {
    return config
}
module.exports = {
    getConfig: getConfig
};