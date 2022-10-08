<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Login</title>
    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">

    <meta name="theme-color" content="#563d7c">


    <style>
        .button {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            transition-duration: 0.4s;
        }

        .button:hover {
            background-color: #4CAF50; /* Green */
            color: white;
        }

        .error {
            color: #ff0000;
        }

        .errorblock {
            color: #000;
            background-color: #ffEEEE;
            border: 3px solid #ff0000;
            padding: 8px;
            margin: 16px;
        }
    </style>
    <!-- Custom styles for this template -->
    <link href="navbar-top.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
<%--    <a class="navbar-brand" href="/store/product-catalog">Back</a>--%>
    <a class="navbar-brand" href="/store/menu">Menu</a>
    <a class="navbar-brand" href="/store/cart">Cart</a>
</nav>

<div class="container">
    <div>
        <div>
            <h1>Cart details</h1>
        </div>

        <c:if test="${not empty negativeQuantity}">
            <div class="alert alert-warning" role="alert">
                    ${negativeQuantity}
            </div>
        </c:if>

        <c:if test="${not empty incorrectQuantity}">
            <div class="alert alert-danger" role="alert">
                    ${incorrectQuantity}
            </div>
        </c:if>

        <c:if test="${not empty invalidInput}">
            <div class="alert alert-danger" role="alert">
                    ${invalidInput}
            </div>
        </c:if>

        <c:if test="${empty orderEntries}">
            <h2><br>Emptiness. Add products to the cart first. </h2>
            <a href="product-catalog" class="btn btn-lg btn-primary" >Product catalog</a><br>
        </c:if>

        <c:if test="${not empty orderEntries}">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Product</th>
                    <th scope="col">Price</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Summary price</th>
                    <th scope="col">Edit quantity</th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <c:forEach var="entry" items="${orderEntries}">
                    <tr>
                        <td>${entry.product.productName}</td>
                        <td>${entry.product.price}</td>
                        <td>${entry.quantity}</td>
                        <td>${entry.product.price * entry.quantity}</td>
                        <td>
                            <form action="/store/cart/edit?productId=${entry.product.id}"  method="post">
                                <input type="text" id="newQuantity" name="newQuantity" class="form-control"><br>
                                <input type="submit" value="Save" class="btn btn-info btn-block"/>
                            </form>
                        </td>
                        <td>
                            <form action="/store/cart/remove-item/${entry.product.id}"  method="post">
                                <input type="submit" value="Delete" class="btn btn-danger btn-block"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <div>
                <h4>Total</h4>
                <h2>${total}</h2> <br>
            </div>

            <form action="proceed-order" method="post">
                <button type="submit" class="btn btn-primary btn-lg" >Place order</button>
            </form>

        </c:if>
    </div>


</div>
</body>
</html>