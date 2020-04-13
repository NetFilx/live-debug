import { Button, Card, Input, Form } from 'antd';
import { connect, Dispatch } from 'umi';
import React, { FC } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { DebuggerData } from './data';

const FormItem = Form.Item;
const { TextArea } = Input;

interface DebuggerProps {
  debug: DebuggerData,
  submitting: boolean;
  dispatch: Dispatch;
}

const Debugger: FC<DebuggerProps> = (props) => {
  const { submitting, debug = {} } = props;
  const { result } = debug;
  const [form] = Form.useForm();
  
  const handleExecute = () => {
    const script = form.getFieldValue('script');
    const { dispatch } = props;
    dispatch({
      type: 'debug/getExecuteResult',
      payload: script,
    });
  }

  return (
    <PageHeaderWrapper>
      <Card bordered={false} title='快捷选项'>
        <Button
          type="primary"
          loading={submitting}
          onClick={handleExecute}
        >
          运行
        </Button>
      </Card>
      <Card bordered={false} style={{ marginTop: 24 }}>
        <Form
          hideRequiredMark
          style={{ marginTop: 8 }}
          form={form}
          name="basic"
        >
          <FormItem
            name="script"
            rules={[
              {
                required: true,
                message: '请填写脚本',
              }
            ]}
          >
            <TextArea
              style={{ minHeight: 32 }}
              placeholder='请填写待提交待的脚本'
              rows={12}
            />
          </FormItem>
        </Form>
      </Card>
      <Card bordered={false} title='运行结果' style={{ marginTop: 24 }}>
        <TextArea
          style={{ minHeight: 32 }}
          value={result}
          rows={4}
        />
      </Card>
    </PageHeaderWrapper>
  );
};

export default connect(
  ({
    debug,
    loading,
  }: {
    debug: DebuggerData;
    loading: {
      effects: {
        [key: string]: boolean;
      };
    };
  }) => ({
    debug,
    submitting: loading.effects['debug/getExecuteResult'],
  }),
)(Debugger);
