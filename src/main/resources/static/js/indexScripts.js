window.onload = function () {
    setTimeout(function(){
        let array = document.querySelectorAll('div.card');
        for(let i=0;i<array.length;i++){
            let details = array[i].querySelector('form').querySelector('div.card-body').querySelectorAll('input');
            let price = array[i].querySelector('form').querySelector('ul').querySelector('li').querySelector('input');
            for(let j=0;j<details.length;j++){
                details[j].setAttribute("value",details[j].getAttribute("placeholder"));
            }
            price.setAttribute("value",price.getAttribute("placeholder"));
        }
    },100);

}