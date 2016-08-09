if (!('Datalogic' in window)) window.Datalogic = {};
Datalogic.BarcodeGenerator = {};
var ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
    ALPHABET_LEN = 37;

Datalogic.BarcodeGenerator.generate = function(text, sym) {
    if (!('BarGen' in window)) throw 'Cannot find a Barcode Generation library';
    
    var barcode = new Datalogic.Barcode({
        code: text,
        type: sym
    });
    barcode.bars = BarGen.generate(text, sym).trim().split(' ');
    return barcode;
};

Datalogic.BarcodeGenerator.randomCode = function(len) {
    len = len || 8;
    var text = '';

    switch (Math.floor(Math.random()*4)) {
        case 0:
            for (var i=0; i < len; i++) {
                text += ALPHABET.charAt(Math.floor(Math.random() * ALPHABET_LEN));
            }
            break;

        case 1:
            text = 'Something with \' 字 and other §hìtty char';
            break;

        case 2:
            text = 'http://www.datalogic.com';
            break;

        case 3:
            text = 'intent://scan/#Intent;package=com.google.zxing.client.android;scheme=zxing;end;';
            break;

        default: break;
    }

    return text;
}

Datalogic.BarcodeGenerator.random = function(code, sym) {
    var barsCount = 51;
    var bars = [];

    for (var i=0; i < barsCount; i++) {
        bars.push( Math.round(Math.random() * 4) + 1 );
    }

    return new Datalogic.Barcode({
        code: code,
        type: sym,
        bars: bars
    });
}
