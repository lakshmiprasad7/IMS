// import React, { useState, useEffect } from "react";
// import Layout from "../component/Layout";
// import { FaEdit, FaTrash, FaPlus } from "react-icons/fa"; // Importing react-icons
// import ApiService from "../service/ApiService";
// import { useNavigate } from "react-router-dom";
// import PaginationComponent from "../component/PaginationComponent";


// const ProductPage = () => {
//   const [products, setProducts] = useState([]);
//   const [message, setMessage] = useState("");

//   const navigate = useNavigate();

//   //Pagination Set-Up
//   const [currentPage, setCurrentPage] = useState(1);
//   const [totalPages, setTotalPages] = useState(0);
//   const itemsPerPage = 8;

//   useEffect(() => {
//     const getProducts = async () => {
//       try {
//         const productData = await ApiService.getAllProducts();

//         if (productData.status === 200) {
//           setTotalPages(Math.ceil(productData.products.length / itemsPerPage));

//           setProducts(
//             productData.products.slice(
//               (currentPage - 1) * itemsPerPage,
//               currentPage * itemsPerPage
//             )
//           );
//         }
//       } catch (error) {
//         showMessage(
//           error.response?.data?.message || "Error Getting Products: " + error
//         );
//       }
//     };

//     getProducts();
//   }, [currentPage]);

//   //Delete a product
//   const handleDeleteProduct = async (productId) => {
//     if (window.confirm("Are you sure you want to delete this Product?")) {
//       try {
//         await ApiService.deleteProduct(productId);
//         showMessage("Product sucessfully Deleted");
//         window.location.reload(); //relode page
//       } catch (error) {
//         showMessage(
//           error.response?.data?.message ||
//             "Error Deleting in a product: " + error
//         );
//       }
//     }
//   };

//   //method to show message or errors
//   const showMessage = (msg) => {
//     setMessage(msg);
//     setTimeout(() => {
//       setMessage("");
//     }, 4000);
//   };

//   return (
//     <Layout>
//       {message && <div className="message">{message}</div>}

//       <div className="product-page">
//         <div className="product-header">
//           <h1>Products</h1>
//           <button
//             className="add-product-btn"
//             onClick={() => navigate("/add-product")}
//           >
//            <FaPlus />  Add Product
//           </button>
//         </div>

//         {products && (
//           <div className="product-list">
//             {products.map((product) => (
//               <div key={product.id} className="product-item">
//                <img
//     className="product-image"
//     src={product.imageUrl} // Correct path for accessing the image
//     alt={product.name}
// />


//                 <div className="product-info">
                
//                     <h3 className="name">{product.name}</h3>
//                     <p className="sku">Sku: {product.sku}</p>
//                     <p className="price">Price: {product.price}</p>
//                     <p className="quantity">Quantity: {product.stockQuantity}</p>
//                 </div>

//                 <div className="product-actions">
//                     <button className="edit-btn" onClick={()=> navigate(`/edit-product/${product.id}`)}> <FaEdit />Edit</button>
//                     <button  className="delete-btn" onClick={()=> handleDeleteProduct(product.id)}> <FaTrash />Delete</button>
//                 </div>
//               </div>
//             ))}
//           </div>
//         )}
//       </div>

//       <PaginationComponent
//       currentPage={currentPage}
//       totalPages={totalPages}
//       onPageChange={setCurrentPage}
//       />
//     </Layout>
//   );
// };
// export default ProductPage;






import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate } from "react-router-dom";
import PaginationComponent from "../component/PaginationComponent";
 
const ProductPage = () => {
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const itemsPerPage = 10;
 
  const navigate = useNavigate();
 
  useEffect(() => {
    const getProducts = async () => {
      try {
        const productData = await ApiService.getAllProducts();
 
        if (productData.status === 200) {
          setTotalPages(Math.ceil(productData.products.length / itemsPerPage));
 
          // Update products based on the current page
          setProducts(
            productData.products.slice(
              (currentPage - 1) * itemsPerPage,
              currentPage * itemsPerPage
            )
          );
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error Getting Products: " + error
        );
      }
    };
 
    getProducts();
  }, [currentPage]);
 
  const handleDeleteProduct = async (productId) => {
    if (window.confirm("Are you sure you want to delete this Product?")) {
      try {
        await ApiService.deleteProduct(productId);
        showMessage("Product successfully deleted");
 
        // Update the product list without reloading the page
        setProducts(products.filter((product) => product.id !== productId));
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error deleting the product: " + error
        );
      }
    }
  };
 
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };
 
  return (
    <Layout>
      {message && <div className="message">{message}</div>}
 
      <div className="product-page">
        <div className="product-header">
          <h1>Products</h1>
          <button
            className="add-product-btn"
            onClick={() => navigate("/add-product")}
          >
           <FaPlus /> Add Product
          </button>
        </div>
 
        {products.length > 0 ? (
          <div className="product-list">
            {products.map((product) =>
               (
              <div key={product.id} className="product-item">
                <img
                  className="product-image"
                  src={product.imageUrl}
                  alt={product.name}
                />
 
                <div className="product-info">
                  <h3 className="name">{product.name}</h3>
                  <p className="sku">Sku: {product.sku}</p>
                  <p className="price">Price: {product.price}</p>
                  <p className="quantity">Quantity: {product.stockQuantity}</p>
                </div>
 
                <div className="product-actions">
                  <button
                    className="edit-btn"
                    onClick={() => navigate(`/edit-product/${product.id}`)}
                  >
                    <FaEdit />Edit
                  </button>
                  <button
                    className="delete-btn"
                    onClick={() => handleDeleteProduct(product.id)}
                  >
                   <FaTrash /> Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p>No products available. Please add some products.</p>
        )}
      </div>
 
      <PaginationComponent
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
    </Layout>
   
  );
 
};
 
export default ProductPage;
 
