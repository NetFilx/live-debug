import request from '@/utils/request';

export async function getExecuteResult(params?: string) {
  return request('/debug/execute', {
    method: 'POST',
    data: params,
  });
}
