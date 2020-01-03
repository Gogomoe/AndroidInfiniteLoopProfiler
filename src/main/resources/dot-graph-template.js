new Viz()
    .renderSVGElement(`{{dot-graph}}`)
    .then(function (element) {
        element.setAttribute("width", "800");
        element.setAttribute("height", "600");
        document.querySelector("#{{dot-graph-id}}").appendChild(element);
    });
