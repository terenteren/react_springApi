import axios from "axios";
import { useLoaderData, type LoaderFunctionArgs } from "react-router";
import ReadComponent from "../../components/products/readComponent";

export async function loadProduct({ params }: LoaderFunctionArgs) {
  const { pno } = params;
  const res = await axios.get(`http://localhost:8080/api/products/${pno}`);
  return res.data;
}

function ReadPage() {
  const product: ProductDTO = useLoaderData();

  return (
    <div className="w-full">
      <div>Product Read</div>
      <ReadComponent product={product}></ReadComponent>
    </div>
  );
}

export default ReadPage;
