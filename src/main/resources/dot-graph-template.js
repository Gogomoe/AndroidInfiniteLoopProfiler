new Viz()
    .renderString(`{{dot-graph}}`)
    .then(function (string) {
        let base64 = btoa(string);
        let img = new Image(800, 600);
        img.src = "data:image/svg+xml;base64," + base64;
        document.querySelector("#{{dot-graph-id}}").appendChild(img);
    });