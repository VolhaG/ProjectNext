
window.open("http://tut.by")

document.getElementById("search_from_str").value = "лукашенко"
document.getElementsByName("search")[0].click()

var el = document.getElementsByClassName("b-results__li m-market")[0] 
el.getElementsByTagName("b")[0].innerText
