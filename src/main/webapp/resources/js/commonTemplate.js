const fn_makeTr = (dataArr = [], tagType = "td") => {
    return dataArr.reduce((p, n) => {
        const $tr = document.createElement(tagType);
        $tr.innerText = n;
        p.appendChild($tr);
        return p;
    }, document.createElement("tr"));
}