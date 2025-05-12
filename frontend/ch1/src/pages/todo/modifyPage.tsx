import { useParams } from "react-router";
import ModifyComponent from "../../components/todo/modifyComponent";

function ModifyPage() {
  const { tno } = useParams();

  return (
    <div className="bg-white w-full">
      <div className="text-4xl"> Todo ModifyPage Page {tno}</div>
      <ModifyComponent tno={Number(tno)} />
    </div>
  );
}

export default ModifyPage;
