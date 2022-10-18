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
        <a class="navbar-brand" href="/store/menu">Menu</a>
        <a class="navbar-brand" href="/store/cart">Cart</a>
    </nav>

    <div class="container">
        <div>
            <h1>Products</h1>
        </div>

        <div>
            <c:if test="${not empty products}">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Product name</th>
                    <th scope="col">Available pcs</th>
                    <th scope="col">Price</th>
                </tr>
                </thead>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.productName}</td>
                            <td>${product.quantityStock}</td>
                            <td>${product.price}</td>
                        </tr>
                    </c:forEach>
            </table>
            </c:if>
        </div>

        <div>
            <h1>Order product</h1>
        </div>

        <div id="prompts">
            <c:if test="${not empty invalidInput}">
                <div class="alert alert-danger" role="alert">
                        ${invalidInput}
                </div>
            </c:if>

            <c:if test="${not empty incorrectQuantity}">
                <div class="alert alert-danger" role="alert">
                    ${incorrectQuantity}
                </div>
            </c:if>

            <c:if test="${not empty productAlreadyInCart}">
                <div class="alert alert-warning" role="alert">
                        ${productAlreadyInCart}
                </div>
            </c:if>

            <c:if test="${not empty negativeQuantity}">
                <div class="alert alert-warning" role="alert">
                        ${negativeQuantity}
                </div>
            </c:if>

            <c:if test="${not empty incorrectProductId}">
                <div class="alert alert-warning" role="alert">
                        ${incorrectProductId}
                </div>
            </c:if>
        </div>

        <div>
            <form method="post">
                    <label for="productId">Product ID</label>
                    <input type="text" class="form-control" name="productId" id="productId" aria-describedby="emailHelp" placeholder="Enter product ID">
                    <label for="productQuantity">Product quantity</label>
                    <input type="text" class="form-control" name="quantity" id="productQuantity" placeholder="Enter quantity"><br>
                    <button type="submit" class="btn btn-primary">Add to cart</button>
            </form>
            <a href="cart" class="btn btn-secondary">Show cart</a><br>
        </div>
    </div>
</body>
</html>