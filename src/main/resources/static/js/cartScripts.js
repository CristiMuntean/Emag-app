window.onload = function () {
    setTimeout(function(){
        let array = document.querySelectorAll('form');
        console.log(array);
        for(let i=1;i<array.length;i++){
            let inputs = array[i].querySelectorAll('input');
            for(let j=0;j<array[i].length-1;j++){
                inputs[j].setAttribute("value",inputs[j].getAttribute("placeholder"));
            }
        }
    },100);

}