import { lazy, Suspense } from "react";
import { Navigate } from "react-router";
import { loadProduct } from "../pages/products/readPage";
import { loadProducts } from "../pages/products/listPage";

const ProductsIndex = lazy(() => import("../pages/products/indexPage"));
const Loading = () => <div>Products Loading....</div>;

const ProductsList = lazy(() => import("../pages/products/listPage"));
const ProductsAdd = lazy(() => import("../pages/products/addPage"));
const ProductsRead = lazy(() => import("../pages/products/readPage"));
const ProductModify = lazy(() => import("../pages/products/modifyPage"));

export default function productsRouter() {
  return {
    path: "products",
    Component: ProductsIndex,
    children: [
      {
        path: "list",
        element: (
          <Suspense fallback={<Loading />}>
            <ProductsList />
          </Suspense>
        ),
        loader: loadProducts,
      },
      {
        path: "",
        element: <Navigate to="/products/list" replace={true} />,
      },
      {
        path: "add",
        element: (
          <Suspense fallback={<Loading />}>
            <ProductsAdd />
          </Suspense>
        ),
      },
      {
        path: "read/:pno",
        element: (
          <Suspense fallback={<Loading />}>
            <ProductsRead />
          </Suspense>
        ),
        loader: loadProduct,
      },
      {
        path: "modify/:pno",
        element: (
          <Suspense fallback={<Loading />}>
            <ProductModify />
          </Suspense>
        ),
        loader: loadProduct,
      },
    ],
  };
}
