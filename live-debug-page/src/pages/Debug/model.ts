import { Effect, Reducer } from "umi";
import { message } from "antd";
import { DebuggerData } from "./data";
import { getExecuteResult } from "./service";



export interface ModelType {
  namespace: string,
  state: DebuggerData,
  effects: {
    getExecuteResult: Effect
  };
  reducers: {
    save: Reducer<DebuggerData>
  }
}

const initDebugData = {
  script: '',
  result: '',
}

const Model: ModelType = {
  namespace: 'debug',

  state: initDebugData,

  effects: {
    *getExecuteResult({ payload }, { call, put }) {
      const res = yield call(getExecuteResult, payload);
      if (!res.success) {
        message.error('脚本执行失败，请检查');
        return;
      }
      res.data = payload;
      yield put({
        type: 'save',
        payload: {
          script: payload,
          result: res.data,
        }
      })
    },
  },
  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
  }

}

export default Model;