import axios from "axios";
import {
  createSearchParams,
  useLoaderData,
  type LoaderFunctionArgs,
} from "react-router";
import ListComponent from "../../components/products/listComponent";

export async function loadProducts({ request }: LoaderFunctionArgs) {
  const url = new URL(request.url);
  const page = url.searchParams.get("page") || "1";
  const size = url.searchParams.get("size") || "10";

  const queryStr = createSearchParams({ page, size }).toString();

  const res = await axios.get(
    `http://localhost:8080/api/products/list?${queryStr}`
  );

  return res.data;
}

const ListPage = () => {
  const pageResponse = useLoaderData();

  console.log("pageResponse", pageResponse);

  return (
    <div className="w-full mt-4 border border-solid border-neutral-300 shadow-md">
      <div className="text-2xl m-4 font-extrabold">Products List Page</div>
      <ListComponent serverData={pageResponse}></ListComponent>
    </div>
  );
};

export default ListPage;
