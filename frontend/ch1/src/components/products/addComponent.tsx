import axios from "axios";
import PendingModal from "../common/pendingModal";
import ResultModal from "../common/resultModal";
import useCustomMove from "../../hooks/useCustomMove";
import { useActionState } from "react";

interface ProductAddResult {
  result?: number;
  error?: string;
}

const initState: ProductAddResult = {
  result: 0,
  error: "",
};

// 액션처리 함수
const addAsyncAction = async (state: ProductAddResult, formData: FormData) => {
  console.log("addAsyncAction...");

  await new Promise((resolve) => setTimeout(resolve, 1000));

  const pname = formData.get("pname") as string;
  if (!pname) {
    return { result: 0, error: "Insert Product Name" };
  }

  const res = await axios.post("http://localhost:8080/api/products/", formData);

  return { result: res.data.result };
};

function AddComponent() {
  const [state, action, isPending] = useActionState(addAsyncAction, initState);

  const { moveToList } = useCustomMove();

  const closeModal = () => {
    if (state.error) {
      alert(state.error);
      return;
    }
    if (state.result) {
      // 등록 후 목록으로 이동
      moveToList();
    }
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {isPending && <PendingModal />}

      {state.result !== 0 && (
        <ResultModal
          title="등록 처리 완료"
          content={`새로운 ${state.result} 상품이 등록되었습니다.`}
          callbackFn={closeModal}
        />
      )}

      <form action={action}>
        <div className="flex justify-center">
          <div className="relative mb-4 flex w-full flex-wrap items-stretch">
            <div className="w-1/5 p-6 text-right font-bold">Product Name</div>

            <input
              className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
              name="pname"
              required
            ></input>
          </div>
        </div>

        <div className="flex justify-center">
          <div className="relative mb-4 flex w-full flex-wrap items-stretch">
            <div className="w-1/5 p-6 text-right font-bold">Desc</div>

            <textarea
              className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
              name="pdesc"
              rows={4}
              required
            ></textarea>
          </div>
        </div>
        <div className="flex justify-center">
          <div className="relative mb-4 flex w-full flex-wrap items-stretch">
            <div className="w-1/5 p-6 text-right font-bold">Price</div>

            <input
              className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
              name="price"
              type={"number"}
              required
            ></input>
          </div>
        </div>

        <div className="flex justify-center">
          <div className="relative mb-4 flex w-full flex-wrap items-stretch">
            <div className="w-1/5 p-6 text-right font-bold">Files</div>

            <input
              className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
              type={"file"}
              name="files"
              multiple={true}
            ></input>
          </div>
        </div>

        <div className="flex justify-end">
          <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
            <button
              type="submit"
              className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
            >
              ADD
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default AddComponent;
